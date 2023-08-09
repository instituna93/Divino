import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../instrument-request.test-samples';

import { InstrumentRequestFormService } from './instrument-request-form.service';

describe('InstrumentRequest Form Service', () => {
  let service: InstrumentRequestFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InstrumentRequestFormService);
  });

  describe('Service methods', () => {
    describe('createInstrumentRequestFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInstrumentRequestFormGroup();

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
            isReturned: expect.any(Object),
            instrument: expect.any(Object),
            member: expect.any(Object),
          })
        );
      });

      it('passing IInstrumentRequest should create a new form with FormGroup', () => {
        const formGroup = service.createInstrumentRequestFormGroup(sampleWithRequiredData);

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
            isReturned: expect.any(Object),
            instrument: expect.any(Object),
            member: expect.any(Object),
          })
        );
      });
    });

    describe('getInstrumentRequest', () => {
      it('should return NewInstrumentRequest for default InstrumentRequest initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInstrumentRequestFormGroup(sampleWithNewData);

        const instrumentRequest = service.getInstrumentRequest(formGroup) as any;

        expect(instrumentRequest).toMatchObject(sampleWithNewData);
      });

      it('should return NewInstrumentRequest for empty InstrumentRequest initial value', () => {
        const formGroup = service.createInstrumentRequestFormGroup();

        const instrumentRequest = service.getInstrumentRequest(formGroup) as any;

        expect(instrumentRequest).toMatchObject({});
      });

      it('should return IInstrumentRequest', () => {
        const formGroup = service.createInstrumentRequestFormGroup(sampleWithRequiredData);

        const instrumentRequest = service.getInstrumentRequest(formGroup) as any;

        expect(instrumentRequest).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInstrumentRequest should not enable id FormControl', () => {
        const formGroup = service.createInstrumentRequestFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInstrumentRequest should disable id FormControl', () => {
        const formGroup = service.createInstrumentRequestFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
