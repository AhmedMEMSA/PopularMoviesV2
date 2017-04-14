package com.mal.ahmed.popularmovies;

import ckm.simple.sql_provider.UpgradeScript;
import ckm.simple.sql_provider.annotation.ProviderConfig;
import ckm.simple.sql_provider.annotation.SimpleSQLConfig;

/**
 * Created by ahmed on 4/9/2016.
 */
@SimpleSQLConfig(
        name = "MovieProvider",
        authority = "com.mal.ahmed.popularmovies.movie_provider.authority",
        database = "movie_favorite_db.db",
        version = 1)

public class MovieConfig implements ProviderConfig {


    @Override
    public UpgradeScript[] getUpdateScripts() {
        return new UpgradeScript[0];
    }
}
