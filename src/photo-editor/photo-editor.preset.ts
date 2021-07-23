import {
  EditorBrushOption,
  EditorEffect,
  FilterTypes,
} from './photo-editor.types';

export const DefaultBrushOptions = Object.freeze<EditorBrushOption>({
  enabled: false,
  opacity: 95,
  color: '#fff',
  size: 10,
  erases: false,
  eraserSize: 20,
});

export const DefaultEffectOptions = Object.freeze<EditorEffect>({
  effect: FilterTypes.FISHEYE,
});
