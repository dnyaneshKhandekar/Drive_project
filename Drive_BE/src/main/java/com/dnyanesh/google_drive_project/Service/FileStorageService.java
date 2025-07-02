package com.dnyanesh.google_drive_project.Service;

import com.dnyanesh.google_drive_project.Entity.FileEntity;
import com.dnyanesh.google_drive_project.Repo.FileRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FileStorageService {

    @Value("${file.upload-dir}")
    private String UploadDir;

    private final FileRepository fileRepository;

    public FileStorageService(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    public String saveFile(MultipartFile file, Long parentFolderId) throws IOException {
        Path uploadPath = Paths.get(UploadDir);
        if(!Files.exists(uploadPath))
        {
            Files.createDirectories(uploadPath);
        }
        //filename
        String fileName=file.getOriginalFilename();
        assert fileName != null;
        Path filePath=uploadPath.resolve(fileName);
        Files.copy(file.getInputStream(),filePath, StandardCopyOption.REPLACE_EXISTING);

        // meta data for save file
        FileEntity fileEntity=new FileEntity();
        fileEntity.setName(fileName);
        fileEntity.setPath(filePath.toString());
        fileEntity.setSize(file.getSize());
        fileEntity.setType("file");
        fileEntity.setParentFolderId(parentFolderId);
        fileEntity.setCreatedAt(LocalDateTime.now());

        fileRepository.save(fileEntity);
        return "File uploaded Successfully!";

    }

    public List<FileEntity> getFilesInFolder(Long parentFolderId)
    {
        if(parentFolderId==null)
        {
            return fileRepository.findAll()
                    .stream()
                    .filter(f->f.getParentFolderId()==null)
                    .collect(Collectors.toList());
        }
        else{
            return fileRepository.findAll()
                    .stream()
                    .filter(f->parentFolderId.equals(f.getParentFolderId()))
                    .collect(Collectors.toList());
        }
    }

    public FileEntity getFileById(Long id)
    {
        return fileRepository.findById(id).orElseThrow(()->new RuntimeException("File not found"));
    }

    public void deleteById(Long id)
    {
        fileRepository.deleteById(id);
    }

}
