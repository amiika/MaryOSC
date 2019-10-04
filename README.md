# MaryOSC
Simple OSC server for MaryTTS Text to Speech Synthesis Platform

# Requirements

Java JDK 1.8

# Run

Run server in port 9000:
```
gradlew run --console=plain"
```

or in custom port:
```
gradlew run --console=plain --args="8004"
```

or create distributable under /build/distributions
```
gradlew build
```

# Usage

**/mary/say** 

1 Argument: string. Speaks given string.

**/mary/save**

2 string arguments: path and text. Saves spoken text to given path. Use full path name or home directory like '~/samples/great.wav'.

**/mary/voice**

1 Argument: int or string. Changes voice with index or voice name. List available voices using '-v' in console.

**/mary/effects**

1 Argument: string. Adds effects to the synthetiser. List available effects using '-e' in console.

**/mary/locale**

1 Argument: string. Changes locale. List available locales using '-l' in console.

# Credits

Thank [Mary](https://github.com/marytts/marytts), obviously.
