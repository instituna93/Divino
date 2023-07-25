import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../tag-type.test-samples';

import { TagTypeFormService } from './tag-type-form.service';

describe('TagType Form Service', () => {
  let service: TagTypeFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(TagTypeFormService);
  });

  describe('Service methods', () => {
    describe('createTagTypeFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createTagTypeFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdBy: expect.any(Object),
            createdOn: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedOn: expect.any(Object),
            deletedBy: expect.any(Object),
            deletedOn: expect.any(Object),
            description: expect.any(Object),
            restricted: expect.any(Object),
            defaultTag: expect.any(Object),
          })
        );
      });

      it('passing ITagType should create a new form with FormGroup', () => {
        const formGroup = service.createTagTypeFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdBy: expect.any(Object),
            createdOn: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedOn: expect.any(Object),
            deletedBy: expect.any(Object),
            deletedOn: expect.any(Object),
            description: expect.any(Object),
            restricted: expect.any(Object),
            defaultTag: expect.any(Object),
          })
        );
      });
    });

    describe('getTagType', () => {
      it('should return NewTagType for default TagType initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createTagTypeFormGroup(sampleWithNewData);

        const tagType = service.getTagType(formGroup) as any;

        expect(tagType).toMatchObject(sampleWithNewData);
      });

      it('should return NewTagType for empty TagType initial value', () => {
        const formGroup = service.createTagTypeFormGroup();

        const tagType = service.getTagType(formGroup) as any;

        expect(tagType).toMatchObject({});
      });

      it('should return ITagType', () => {
        const formGroup = service.createTagTypeFormGroup(sampleWithRequiredData);

        const tagType = service.getTagType(formGroup) as any;

        expect(tagType).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing ITagType should not enable id FormControl', () => {
        const formGroup = service.createTagTypeFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewTagType should disable id FormControl', () => {
        const formGroup = service.createTagTypeFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
