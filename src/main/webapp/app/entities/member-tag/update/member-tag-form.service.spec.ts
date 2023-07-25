import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../member-tag.test-samples';

import { MemberTagFormService } from './member-tag-form.service';

describe('MemberTag Form Service', () => {
  let service: MemberTagFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(MemberTagFormService);
  });

  describe('Service methods', () => {
    describe('createMemberTagFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createMemberTagFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdBy: expect.any(Object),
            createdOn: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedOn: expect.any(Object),
            deletedBy: expect.any(Object),
            deletedOn: expect.any(Object),
            tag: expect.any(Object),
            member: expect.any(Object),
          })
        );
      });

      it('passing IMemberTag should create a new form with FormGroup', () => {
        const formGroup = service.createMemberTagFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdBy: expect.any(Object),
            createdOn: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedOn: expect.any(Object),
            deletedBy: expect.any(Object),
            deletedOn: expect.any(Object),
            tag: expect.any(Object),
            member: expect.any(Object),
          })
        );
      });
    });

    describe('getMemberTag', () => {
      it('should return NewMemberTag for default MemberTag initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createMemberTagFormGroup(sampleWithNewData);

        const memberTag = service.getMemberTag(formGroup) as any;

        expect(memberTag).toMatchObject(sampleWithNewData);
      });

      it('should return NewMemberTag for empty MemberTag initial value', () => {
        const formGroup = service.createMemberTagFormGroup();

        const memberTag = service.getMemberTag(formGroup) as any;

        expect(memberTag).toMatchObject({});
      });

      it('should return IMemberTag', () => {
        const formGroup = service.createMemberTagFormGroup(sampleWithRequiredData);

        const memberTag = service.getMemberTag(formGroup) as any;

        expect(memberTag).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IMemberTag should not enable id FormControl', () => {
        const formGroup = service.createMemberTagFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewMemberTag should disable id FormControl', () => {
        const formGroup = service.createMemberTagFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
