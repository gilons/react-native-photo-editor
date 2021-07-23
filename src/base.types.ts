import type { View, ViewProps } from 'react-native';

export interface BaseNativeModulePropsTypes extends ViewProps {}

export type BaseNativeComponentType<T> =
  | React.ComponentType<T>
  | undefined
  | typeof View;
