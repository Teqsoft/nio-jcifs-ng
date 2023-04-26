package io.github.teqsoft.nio.smb;

import java.io.IOException;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileTime;
import java.util.concurrent.TimeUnit;

import jcifs.smb.SmbFile;

public final class SMBFileAttributeView implements BasicFileAttributeView {

    /** {@link SmbFile} reference used by the current instance of {@link SMBFileAttributeView}. */
    private final SMBPath path;

    /**
     * Public default constructor.
     *
     * @param path {@link SMBPath} for which to create {@link SMBFileAttributeView}.
     */
    public SMBFileAttributeView(SMBPath path) {
        this.path = path;
    }

    /**
     * Returns the name of {@link SMBFileAttributeView}, which is 'basic'.
     *
     * @return 'basic'
     */
    @Override
    public String name() {
        return "basic";
    }

    /**
     * Reads the {@link SmbFile}'s attributes and returns them in the form of an {@link SMBFileAttributes} instance.
     *
     * @return {@link SMBFileAttributes}
     * @throws IOException If reading the file's attributes fails.
     */
    @Override
    public BasicFileAttributes readAttributes() throws IOException {
        return new SMBFileAttributes(this.path.getSmbFile());
    }

    @Override
    public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {
        if (lastModifiedTime == null && lastAccessTime == null && createTime == null) {
            return;
        }

        long lastModified = lastModifiedTime != null ? lastModifiedTime.to(TimeUnit.MILLISECONDS) : 0L;
        long lastAccess = lastAccessTime != null ? lastAccessTime.to(TimeUnit.MILLISECONDS) : 0L;
        long create = createTime != null ? createTime.to(TimeUnit.MILLISECONDS) : 0L;

        final SmbFile file = this.path.getSmbFile();
        file.setFileTimes(create, lastModified, lastAccess);
    }
}
