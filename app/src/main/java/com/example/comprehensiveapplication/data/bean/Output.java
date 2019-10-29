package com.example.comprehensiveapplication.data.bean;



public class Output {
    private OutputType outputType ;
    private ApkData apkData;

    public OutputType getOutputType() {
        return outputType;
    }

    public void setOutputType(OutputType outputType) {
        this.outputType = outputType;
    }

    public ApkData getApkData() {
        return apkData;
    }

    public void setApkData(ApkData apkData) {
        this.apkData = apkData;
    }

    public class OutputType {
        private String type;

        public  OutputType() {

        }
        public OutputType(String type) {
            this.type = type;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }
    }
    public class ApkData {
        private String type;
        private String splits;
        private long  versionCode;
        private String versionName;
        private boolean enabled;
        private String outputFile;
        private String fullName;
        private String baseName;

        public  ApkData() {

        }
        public ApkData(String type, String splits, long versionCode, String versionName, boolean enabled, String outputFile, String fullName, String baseName) {
            this.type = type;
            this.splits = splits;
            this.versionCode = versionCode;
            this.versionName = versionName;
            this.enabled = enabled;
            this.outputFile = outputFile;
            this.fullName = fullName;
            this.baseName = baseName;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getSplits() {
            return splits;
        }

        public void setSplits(String splits) {
            this.splits = splits;
        }

        public long getVersionCode() {
            return versionCode;
        }

        public void setVersionCode(long versionCode) {
            this.versionCode = versionCode;
        }

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public boolean isEnabled() {
            return enabled;
        }

        public void setEnabled(boolean enabled) {
            this.enabled = enabled;
        }

        public String getOutputFile() {
            return outputFile;
        }

        public void setOutputFile(String outputFile) {
            this.outputFile = outputFile;
        }

        public String getFullName() {
            return fullName;
        }

        public void setFullName(String fullName) {
            this.fullName = fullName;
        }

        public String getBaseName() {
            return baseName;
        }

        public void setBaseName(String baseName) {
            this.baseName = baseName;
        }
    }
    private String path;
    private String properties;

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getProperties() {
        return properties;
    }

    public void setProperties(String properties) {
        this.properties = properties;
    }
    public  Output(){

    }
    public Output(OutputType outputType,ApkData apkData,String path, String properties) {
        this.outputType = outputType;
        this.apkData = apkData;
        this.path = path;
        this.properties = properties;
    }
}
