package com.nivor.file;
import java.io.*;import java.nio.file.*;import org.springframework.beans.factory.annotation.Value;import org.springframework.stereotype.Service;
public interface FileStorageService{void store(String key,byte[] bytes)throws IOException;InputStream open(String key)throws IOException;void delete(String key)throws IOException;boolean exists(String key)throws IOException;}
@Service class LocalFileStorageService implements FileStorageService{
 private final Path root;private final String provider;
 LocalFileStorageService(@Value("${file.storage.provider:local}")String provider,@Value("${file.storage.local-path:./uploads}")String path)throws IOException{this.provider=provider.toLowerCase();this.root=Path.of(path).toAbsolutePath().normalize();if(this.provider.equals("local"))Files.createDirectories(root);}
 private Path safe(String key)throws IOException{if(!provider.equals("local"))throw new IOException("Object storage is not configured for provider "+provider);Path p=root.resolve(key).normalize();if(!p.startsWith(root))throw new IOException("Invalid storage key");return p;}
 public void store(String key,byte[] b)throws IOException{Path p=safe(key);Files.createDirectories(p.getParent());Files.write(p,b,StandardOpenOption.CREATE_NEW);}
 public InputStream open(String key)throws IOException{return Files.newInputStream(safe(key));}
 public void delete(String key)throws IOException{Files.deleteIfExists(safe(key));}
 public boolean exists(String key)throws IOException{return Files.exists(safe(key));}
}
