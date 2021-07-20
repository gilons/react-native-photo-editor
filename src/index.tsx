import { requireNativeComponent, ViewStyle } from 'react-native';

type PhotoEditorProps = {
  color: string;
  style: ViewStyle;
};

export const PhotoEditorViewManager = requireNativeComponent<PhotoEditorProps>(
'PhotoEditorView'
);

export default PhotoEditorViewManager;
