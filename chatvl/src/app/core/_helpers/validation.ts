import { AbstractControl } from '@angular/forms';

export function urlValidator(control: AbstractControl) {
  // tslint:disable-next-line:max-line-length
  if (!control.value.startsWith('https://www.youtube.com/watch?v=') && !control.value.startsWith('http://www.youtube.com/watch?v=')) {
    return { urlValid: true };
  }
  return null;
}
