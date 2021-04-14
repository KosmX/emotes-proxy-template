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
    //You'll need to implement emotesAPI.
    implementation "io.github.kosmx.emotes:emotesAPI:${project.emotes_api_version}"
    //Don't do modImplementation, emotesApi is NOT a fabric mod.
}

```
Client-side implementation
---
The emotesAPI lets you to implement an Emotecraft proxy mod, but you can't run the client-side proxy without Emotecraft.  
**DO NOT include** this api in your **client-side** mod, as this api is PART of Emotecraft.  
The api is also the same for every Emotecraft versions including Forge versions.  
   
Description about the proxy implementation is in `src/main/java/io.github.kosmx.example.proxy.ExampleNetwork` comments  

Server-side implementation
---
You can just send the data from client to client without any changes.  
 
Buf if you can, and you want to do server-side emote verification/validation there is an example in
`src/main/java/io.github.kosmx.example.proxy.serverSideTemplate.ExampleServerNetwork`
with many comments in the code.  
  
You can use this api at server-side, **without any additional dependency**  
To include emotesAPI in your program you can use [Gradle Shadow Plugin](https://imperceptiblethoughts.com/shadow/).


## License

EmotesAPI is under CC-BY-4.0 License, fell free to include that in your projects.

This template is available under the CC0 license. Feel free to learn from it and incorporate it in your own projects.
