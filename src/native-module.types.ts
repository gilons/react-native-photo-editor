import type {
  PhotoEditorComponentType,
  PhotoEditorNativeModuleType,
  PhotoEditorPropsType,
} from './photo-editor';

export interface NativeComponentsPropsType {
  PhotoEditor: PhotoEditorPropsType;
}

export interface NativeComponentsTypes {
  PhotoEditor: PhotoEditorComponentType;
}

export interface NativeModules {
  PhotoEditor: PhotoEditorNativeModuleType;
}

export interface PhotoEditorTypes {
  PropsTypes: NativeComponentsPropsType;
  ComponentType: NativeComponentsTypes;
  Modules: NativeModules;
}
