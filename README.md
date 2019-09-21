# MaryOSC
Simple OSC server for MaryTTS Text to Speech Synthesis Platform

# Run

Run server using:
```
gradlew run --console=plain
```

Create distributable under /build/distributions
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
