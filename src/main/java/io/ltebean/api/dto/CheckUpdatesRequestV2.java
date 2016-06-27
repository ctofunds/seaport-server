package io.ltebean.api.dto;

import java.util.List;

/**
 * Created by ltebean on 16/5/6.
 */
public class CheckUpdatesRequestV2 {

    public String secret;

    public List<PackageInfo> packageInfos;

    public static class PackageInfo {

        public String name;

        public String version;

        public String deviceInfo;

        public String uuid;
    }
}
