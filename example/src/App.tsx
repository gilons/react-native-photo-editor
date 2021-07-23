import * as React from 'react';

import { StyleSheet, Text, View, TouchableOpacity } from 'react-native';
import PhotoEditorView, {
  EditTextEvent,
  SaveResultEvent,
} from 'react-native-photo-editor';

export default function App() {
  const editorRef = React.useRef<PhotoEditorView>(null);

  const handleSave = React.useCallback((event: SaveResultEvent) => {
    console.warn(event.path);
  }, []);
  const addText = () => {
    editorRef.current?.addText('hello Text', '#000000');
  };
  const onEditText = React.useCallback((data: EditTextEvent) => {
    console.warn(data);
  }, []);
  return (
    <View style={styles.container}>
      <PhotoEditorView
        savePath={'result.png'}
        onEditText={onEditText}
        onSaveResult={handleSave}
        ref={editorRef}
        resizeMode="contain"
        src={{ uri: 'https://i.imgur.com/DvpvklR.png' }}
        style={styles.box}
      />
      <TouchableOpacity style={styles.buttonStyles} onPress={addText}>
        <Text>{'Add Text'}</Text>
      </TouchableOpacity>
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
  buttonStyles: {
    height: 60,
    width: 120,
    elevation: 2,
    backgroundColor: '#fff',
    borderRadius: 5,
    marginTop: 30,
    justifyContent: 'center',
    alignItems: 'center',
  },
});
