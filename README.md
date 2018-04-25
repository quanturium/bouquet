# Bouquet

[![Core](https://api.bintray.com/packages/quanturium/maven/bouquet-plugin/images/download.svg) ](https://bintray.com/quanturium/maven/bouquet-plugin/_latestVersion)
![Platform](https://img.shields.io/badge/platform-Android%20%7C%20Java%20%7C%20Kotlin-lightgrey.svg)
[![Build Status](https://travis-ci.org/quanturium/bouquet.svg?branch=master)](https://travis-ci.org/quanturium/bouquet)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://github.com/quanturium/bouquet/blob/master/LICENSE.txt)

## Description

Bouquet is a java / android library that helps debugging RxJava2 by logging various data for Observables, Flowables, Singles, Maybes and Completables.
* For Android, only debuggable builds will be annotated and wrapped with the debug logic.
* For java, you might want to disable it in your build.gradle file on release builds.

The logged info include:
* Description of the method called
* Life cycle events from RxJava
* A summary of the result: count of items, time, subscribing thread and observing thread

## Setup

```groovy
buildscript {
  repositories {
    mavenCentral()
  }

  dependencies {
    classpath 'com.quanturium.bouquet:bouquet-plugin:1.2.0'
  }
}

apply plugin: 'com.quanturium.bouquet'

// Enable or disable Bouquet. By default enabled=true
bouquet {
  enabled true
}
```

## Usage

Annotate any method returning an Observable, Flowable, Single, Maybe, Completable from RxJava2 with `@RxLogger`

```java
@RxLogger
private static Observable<String> getObservableExample(String extra) {
    return Observable.just("String 1", "String 2", "String 3", extra);
}
```

This will print out the following

![Logs](https://raw.githubusercontent.com/quanturium/bouquet/master/assets/screenshot001.png)

or in Kotlin:

```kotlin
@RxLogger(SUMMARY)
fun getKotlinCompletableExample(): Completable {
    return Completable.complete() // Useless, just for demo purposes
        .subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}
```

### Scopes

You can change the scope of logs in order to log more or less info:

```java
@RxLogger(LIFECYCLE)
```

Possible values:  
```java
enum RxLogger.Scope {ALL, SOURCE, LIFECYCLE, SUMMARY, NONE}
```

### Customization

Enable/disable Bouquet at runtime:
```java
Bouquet.setEnabled(false);
```

By default bouquet uses AndroidLogger() for Android, JavaLogger() for java. You can customize the logger at runtime:
```java
Bouquet.setLogger((tag, message) -> {
    Timber.i(message);
});
```

## Local development

In order to run the sample with the plugin code, the plugin must be pushed to your local maven repository. 
The sample project will then pick up the local version of the plugin and use it.

* `./gradlew install`  install the plugin in your local repo
* `./gradlew cleanSampleAndroid` clean the android sample
* `./gradlew installSampleAndroid` install the android sample on your device
* `./gradlew cleanSampleJava` clean the java sample
* `./gradlew runSampleJava` run the java sample

## Thank you

* Jake Wharton for the Hugo plugin this library was inspired by.
* Fernando Cejas for creating the [frodo](https://github.com/android10/frodo "frodo") / [frodo2](https://github.com/android10/frodo2 "frodo2") plugin. This library was inspired by frodo and made compatible for RxJava2  

## License
    Copyright (c) 2018 Arnaud Frugier

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
