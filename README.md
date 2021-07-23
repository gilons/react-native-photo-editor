# react-native-photo-editor

React Native photo editor is a wrapper around android photo editing library in android and a similar library in IOS

## Installation

```sh
npm install react-native-photo-editor
```

## Usage

```js
import PhotoEditor from "react-native-photo-editor";
import { View } from "react-native"
import { useRef } from "react"

// ...

export const MyEditor = () => {
    const editorRef = useRef<PhotoEditor>()
    const handleSave = (resultUri: string) => {
        console.warn(resultUri)
    }

    return <View style={{height: 400, width:"100%"}}>
    <PhotoEditor 
    savePath={'result.png'} 
    resizeMode="contain" 
    onSaveResult={handleSave}
    src={{uri: 'https://i.imgur.com/DvpvklR.png'}} 
    ref={editorRef}/>
    </View>
}
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT
