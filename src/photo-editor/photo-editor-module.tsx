import React, { Component } from 'react';
import { findNodeHandle } from 'react-native';
import {
  NativeModules,
  Platform,
  requireNativeComponent,
  View,
  StyleSheet,
  UIManager,
} from 'react-native';
import {
  ActionTypes,
  EditTextEvent,
  OnchangeActionTypes,
  PhotoEditorComponentType,
  PhotoEditorNativeModuleType,
  PhotoEditorOnChangeParamsType,
  PhotoEditorPropsType,
  SaveResultEvent,
} from './photo-editor.types';
import {
  DefaultBrushOptions,
  DefaultEffectOptions,
} from './photo-editor.preset';

const EditorModule = Platform.select<PhotoEditorComponentType>({
  android: requireNativeComponent(
    'PhotoEditorView'
  ) as PhotoEditorComponentType,
  ios: View,
});

export const EditorCallBacks =
  NativeModules.PEditorModule as PhotoEditorNativeModuleType;

export class PhotoEditorView extends Component<PhotoEditorPropsType> {
  constructor(props: PhotoEditorPropsType) {
    super(props);
  }
  private previousColor = '#FFF';
  private getEmojiesResolver = null as any;
  private makeNativeCommandRequest = (
    name: string,
    ...args: Array<any>
  ): void => {
    const command =
      UIManager.getViewManagerConfig('PhotoEditorView').Commands[name];
    const node = findNodeHandle(this);
    UIManager.dispatchViewManagerCommand(node, command, args ?? []);
  };
  saveResult = () => {
    this.makeNativeCommandRequest(ActionTypes.SAVE_RESULT);
  };

  resetBrushes = () => {
    this.makeNativeCommandRequest(ActionTypes.RESET_BRUSHES);
  };

  resetEffects = () => {
    this.makeNativeCommandRequest(ActionTypes.RESET_EFFECTS);
  };

  resetEditor = () => {
    this.makeNativeCommandRequest(ActionTypes.RESET_EDITOR);
  };

  undo = () => {
    this.makeNativeCommandRequest(ActionTypes.UNDO);
  };

  redo = () => {
    this.makeNativeCommandRequest(ActionTypes.REDO);
  };

  addPhoto = () => {
    this.makeNativeCommandRequest(ActionTypes.ADD_PHOTO);
  };

  addText = (text: string, color?: string) => {
    color = color || this.previousColor;
    this.previousColor = color;
    this.makeNativeCommandRequest(ActionTypes.ADD_TEXT, text, color);
    this.forceUpdate();
  };

  editText = (text: string, color?: string) => {
    color = color || this.previousColor;
    this.makeNativeCommandRequest(ActionTypes.EDIT_TEXT, text, color);
  };
  getImojies() {
    return new Promise((resolve) => {
      this.getEmojiesResolver = resolve;
      this.makeNativeCommandRequest(ActionTypes.GET_IMOJIES);
    });
  }

  onChange = (event: PhotoEditorOnChangeParamsType) => {
    const type = event.nativeEvent.type;
    switch (type) {
      case OnchangeActionTypes.EDIT_TEXT: {
        console.warn(event);
        const data = event.nativeEvent.data as EditTextEvent;
        this.props.onEditText && this.props.onEditText(data);
        break;
      }
      case OnchangeActionTypes.SAVE_RESULT: {
        const data = event.nativeEvent.data as SaveResultEvent;
        this.props.onSaveResult && this.props.onSaveResult(data);
        break;
      }
      case OnchangeActionTypes.GET_IMOJIES: {
        const data = event.nativeEvent.data as any;
        this.getEmojiesResolver && this.getEmojiesResolver(data);
        break;
      }
      default:
        return;
    }
  };
  render() {
    const { src, resizeMode, savePath, brushOptions, effectOptions } =
      this.props;
    const brush = brushOptions
      ? { ...DefaultBrushOptions, ...brushOptions }
      : DefaultBrushOptions;
    const effects = effectOptions
      ? { ...DefaultEffectOptions, ...effectOptions }
      : DefaultEffectOptions;

    return EditorModule ? (
      <EditorModule
        resizeMode={resizeMode}
        onChange={this.onChange}
        brushOptions={brush}
        effectOptions={effects}
        savePath={savePath}
        src={src}
        style={styles.container}
      />
    ) : (
      <View />
    );
  }
}

const styles = StyleSheet.create({
  container: {
    height: 300,
    marginTop: 20,
    width: '100%',
  },
});
