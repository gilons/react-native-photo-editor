import type {
  ImageResizeMode,
  ImageSourcePropType,
  NativeSyntheticEvent,
} from 'react-native';
import type {
  BaseNativeComponentType,
  BaseNativeModulePropsTypes,
} from '../base.types';

export interface EditorBrushOption {
  enabled: Boolean;
  size?: Number;
  opacity?: number;
  color?: string;
  erases?: Boolean;
  eraserSize?: number;
}

export interface EditorFilterOptions {
  type: string;
}

export enum FilterTypes {
  BRIGHTNESS = 'BRIGHTNESS',
  BLACKWHITE = 'BLACKWHITE',
  AUTOFIX = 'AUTOFIX',
  CONTRAST = 'CONTRAST',
  CROSSPROCESS = 'CROSSPROCESS',
  DUOTONE = 'DUOTONE',
  FILLLIGHT = 'FILLLIGHT',
  FISHEYE = 'FISHEYE',
  GRAIN = 'GRAIN',
  GRAYSCALE = 'GRAYSCALE',
  VIGNETTE = 'VIGNETTE',
  POSTERIZE = 'POSTERIZE',
  SATURATE = 'SATURATE',
  FLIPVERSION = 'FLIPVERSION',
  FLIP_HORIZONTAL = 'FLIP_HORIZONTAL',
  NONE = 'NONE',
  SEPIA = 'SEPIA',
  SHARPEN = 'SHARPEN',
  TEMPERATURE = 'TEMPERATURE',
  TINT = 'TINT',
}

type Effects = `${FilterTypes}`;

export interface EditorEffect {
  effect?: Effects;
  intensity?: number;
}
interface BasePhotoEditorPropsType extends BaseNativeModulePropsTypes {
  src: PhotoSource;
  savePath: string;
  effectOptions?: EditorEffect;
  brushOptions?: EditorBrushOption;
  resizeMode?: ImageResizeMode;
}
export interface PhotoEditorPropsType extends BasePhotoEditorPropsType {
  onEditText?: (result: EditTextEvent) => void;
  onSaveResult?: (event: SaveResultEvent) => void;
}
export interface PhotoEditorNativeModulePropsTpe
  extends BasePhotoEditorPropsType {
  onChange: (event: PhotoEditorOnChangeParamsType) => void;
}
export interface EditTextEvent {
  color: String;
  currentText: string;
}

export interface SaveResultEvent {
  path: string;
}

type PhotoSource = ImageSourcePropType & {};
export enum ActionTypes {
  SAVE_RESULT = 'SAVE_RESULT',
  RESET_EDITOR = 'RESET_EDITOR',
  RESET_BRUSHES = 'RESET_BRUSHES',
  RESET_EFFECTS = 'RESET_EFFECTS',
  UNDO = 'UNDO',
  REDO = 'REDO',
  ADD_TEXT = 'ADD_TEXT',
  ADD_PHOTO = 'ADD_PHOTO',
  EDIT_TEXT = 'EDIT_TEXT',
  GET_IMOJIES = 'GET_IMOJIES',
}
export enum OnchangeActionTypes {
  SAVE_RESULT = 'SAVE_RESULT',
  EDIT_TEXT = 'EDIT_TEXT',
  GET_IMOJIES = 'GET_IMOJIES',
}

export type PhotoEditorOnChangeParamsType = NativeSyntheticEvent<{
  type: `${OnchangeActionTypes}`;
  data: any;
}>;

export interface PhotoEditorRef {
  saveResult(): void;
  resetBrushes(): void;
  resetEffects(): void;
  resetEditor(): void;
  redo(): void;
  undo(): void;
  addPhoto(): void;
  addText(text: string, color?: string): void;
  editText(text: string, color?: string): void;
  getEdit(): void;
  getImojies(): void;
}

export interface PhotoEditorNativeModuleType {
  selectImage(): Promise<String>;
}

export type PhotoEditorComponentType =
  BaseNativeComponentType<PhotoEditorNativeModulePropsTpe> & {};
