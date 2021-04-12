# Emotecraft proxy example Fabric mod

## Setup

I was using Fabric for the template.  
For setup Fabric see the [fabric wiki page](https://fabricmc.net/wiki/tutorial:setup)

## Emotes api  
```groovy
repositories {
    (...)
    maven{
        name = 'KosmX\'s repo'
        url = 'http://kosmx.ddns.net/maven' //I'll probably change it
    }
}

dependencies {
    (...)
    //You'll need to implement emotesAPI. //TODO put the latest version to github.
    implementation "io.github.kosmx.emotes:emotesAPI:${project.emotes_api_version}"
    //Don't do modImplementation, emotesApi is NOT a fabric mod.
}

```

## License

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
