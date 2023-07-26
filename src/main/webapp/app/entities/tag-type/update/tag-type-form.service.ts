import { Injectable } from '@angular/core';
import { FormGroup, FormControl, Validators } from '@angular/forms';

import dayjs from 'dayjs/esm';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';
import { ITagType, NewTagType } from '../tag-type.model';

/**
 * A partial Type with required key is used as form input.
 */
type PartialWithRequiredKeyOf<T extends { id: unknown }> = Partial<Omit<T, 'id'>> & { id: T['id'] };

/**
 * Type for createFormGroup and resetForm argument.
 * It accepts ITagType for edit and NewTagTypeFormGroupInput for create.
 */
type TagTypeFormGroupInput = ITagType | PartialWithRequiredKeyOf<NewTagType>;

/**
 * Type that converts some properties for forms.
 */
type FormValueOf<T extends ITagType | NewTagType> = Omit<T, 'createdOn' | 'updatedOn' | 'deletedOn'> & {
  createdOn?: string | null;
  updatedOn?: string | null;
  deletedOn?: string | null;
};

type TagTypeFormRawValue = FormValueOf<ITagType>;

type NewTagTypeFormRawValue = FormValueOf<NewTagType>;

type TagTypeFormDefaults = Pick<NewTagType, 'id' | 'createdOn' | 'updatedOn' | 'deletedOn' | 'restricted'>;

type TagTypeFormGroupContent = {
  id: FormControl<TagTypeFormRawValue['id'] | NewTagType['id']>;
  createdBy: FormControl<TagTypeFormRawValue['createdBy']>;
  createdOn: FormControl<TagTypeFormRawValue['createdOn']>;
  updatedBy: FormControl<TagTypeFormRawValue['updatedBy']>;
  updatedOn: FormControl<TagTypeFormRawValue['updatedOn']>;
  deletedBy: FormControl<TagTypeFormRawValue['deletedBy']>;
  deletedOn: FormControl<TagTypeFormRawValue['deletedOn']>;
  description: FormControl<TagTypeFormRawValue['description']>;
  restricted: FormControl<TagTypeFormRawValue['restricted']>;
  defaultTag: FormControl<TagTypeFormRawValue['defaultTag']>;
};

export type TagTypeFormGroup = FormGroup<TagTypeFormGroupContent>;

@Injectable({ providedIn: 'root' })
export class TagTypeFormService {
  createTagTypeFormGroup(tagType: TagTypeFormGroupInput = { id: null }): TagTypeFormGroup {
    const tagTypeRawValue = this.convertTagTypeToTagTypeRawValue({
      ...this.getFormDefaults(),
      ...tagType,
    });
    return new FormGroup<TagTypeFormGroupContent>({
      id: new FormControl(
        { value: tagTypeRawValue.id, disabled: true },
        {
          nonNullable: true,
          validators: [Validators.required],
        }
      ),
      createdBy: new FormControl({ value: tagTypeRawValue.createdBy, disabled: true }),
      createdOn: new FormControl({ value: tagTypeRawValue.createdOn, disabled: true }),
      updatedBy: new FormControl({ value: tagTypeRawValue.updatedBy, disabled: true }),
      updatedOn: new FormControl({ value: tagTypeRawValue.updatedOn, disabled: true }),
      deletedBy: new FormControl({ value: tagTypeRawValue.deletedBy, disabled: true }),
      deletedOn: new FormControl({ value: tagTypeRawValue.deletedOn, disabled: true }),
      description: new FormControl(tagTypeRawValue.description, {
        validators: [Validators.required],
      }),
      restricted: new FormControl(tagTypeRawValue.restricted),
      defaultTag: new FormControl(tagTypeRawValue.defaultTag),
    });
  }

  getTagType(form: TagTypeFormGroup): ITagType | NewTagType {
    return this.convertTagTypeRawValueToTagType(form.getRawValue() as TagTypeFormRawValue | NewTagTypeFormRawValue);
  }

  resetForm(form: TagTypeFormGroup, tagType: TagTypeFormGroupInput): void {
    const tagTypeRawValue = this.convertTagTypeToTagTypeRawValue({ ...this.getFormDefaults(), ...tagType });
    form.reset(
      {
        ...tagTypeRawValue,
        id: { value: tagTypeRawValue.id, disabled: true },
      } as any /* cast to workaround https://github.com/angular/angular/issues/46458 */
    );
  }

  private getFormDefaults(): TagTypeFormDefaults {
    const currentTime = dayjs();

    return {
      id: null,
      createdOn: currentTime,
      updatedOn: currentTime,
      deletedOn: currentTime,
      restricted: false,
    };
  }

  private convertTagTypeRawValueToTagType(rawTagType: TagTypeFormRawValue | NewTagTypeFormRawValue): ITagType | NewTagType {
    return {
      ...rawTagType,
      createdOn: dayjs(rawTagType.createdOn, DATE_TIME_FORMAT),
      updatedOn: dayjs(rawTagType.updatedOn, DATE_TIME_FORMAT),
      deletedOn: dayjs(rawTagType.deletedOn, DATE_TIME_FORMAT),
    };
  }

  private convertTagTypeToTagTypeRawValue(
    tagType: ITagType | (Partial<NewTagType> & TagTypeFormDefaults)
  ): TagTypeFormRawValue | PartialWithRequiredKeyOf<NewTagTypeFormRawValue> {
    return {
      ...tagType,
      createdOn: tagType.createdOn ? tagType.createdOn.format(DATE_TIME_FORMAT) : undefined,
      updatedOn: tagType.updatedOn ? tagType.updatedOn.format(DATE_TIME_FORMAT) : undefined,
      deletedOn: tagType.deletedOn ? tagType.deletedOn.format(DATE_TIME_FORMAT) : undefined,
    };
  }
}
