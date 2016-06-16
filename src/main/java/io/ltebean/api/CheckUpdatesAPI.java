package io.ltebean.api;

import com.github.zafarkhaja.semver.Version;
import com.google.common.base.Preconditions;
import io.ltebean.api.dto.CheckUpdatesRequest;
import io.ltebean.api.dto.CheckUpdatesRequestV2;
import io.ltebean.api.dto.Response;
import io.ltebean.mapper.AppMapper;
import io.ltebean.mapper.PackageMapper;
import io.ltebean.model.App;
import io.ltebean.model.Package;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Created by ltebean on 16/5/6.
 */

@RestController
public class CheckUpdatesAPI {

    private Logger logger = LoggerFactory.getLogger(CheckUpdatesAPI.class);

    @Autowired
    PackageMapper packageMapper;

    @Autowired
    AppMapper appMapper;


    @RequestMapping(value = "/api/v1/check_updates", method = RequestMethod.POST)
    public Response checkUpdates(@RequestBody CheckUpdatesRequest request) {
        App app = appMapper.findBySecret(request.secret);
        Response response = new Response();
        if (app == null) {
            response.code = HttpStatus.NOT_FOUND.value();
            return response;
        }
        List<Package> allPackages = packageMapper.findByAppId(app.id);
        List<Package> packages = new ArrayList<>();
        for (CheckUpdatesRequest.PackageRequirement requirement : request.packageRequirements) {
            Optional<Package> satisfied = findLatest(allPackages, requirement.name, Optional.of(requirement.versionRange));
            if (satisfied.isPresent()) {
                packages.add(satisfied.get());
            }
        }
        response.data = packages;
        return response;
    }


    @RequestMapping(value = "/api/v2/check_updates", method = RequestMethod.POST)
    public Response checkUpdates(@RequestBody CheckUpdatesRequestV2 request) {
        App app = appMapper.findBySecret(request.secret);
        Response response = new Response();
        if (app == null) {
            response.code = HttpStatus.NOT_FOUND.value();
            return response;
        }
        List<Package> allPackages = packageMapper.findByAppId(app.id);
        Preconditions.checkArgument(request.packageInfo != null, "package info cannot be empty");
        Optional<Package> satisfied = findLatest(allPackages, request.packageInfo.name, Optional.empty());
        response.data = satisfied.orElse(null);
        return response;
    }

    private static Optional<Package> findLatest(List<Package> packages, String packageName, Optional<String> versionRange) {
        return packages.stream().
                filter(p -> p.name.equals(packageName)).
                filter(p -> versionRange.isPresent() ? Version.valueOf(p.version).satisfies(versionRange.get()) : true).
                max((p1, p2) -> Version.valueOf(p1.version).compareTo(Version.valueOf(p2.version)));
    }


}
