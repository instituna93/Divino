import { TestBed } from '@angular/core/testing';

import { sampleWithRequiredData, sampleWithNewData } from '../instrument.test-samples';

import { InstrumentFormService } from './instrument-form.service';

describe('Instrument Form Service', () => {
  let service: InstrumentFormService;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(InstrumentFormService);
  });

  describe('Service methods', () => {
    describe('createInstrumentFormGroup', () => {
      it('should create a new form with FormControl', () => {
        const formGroup = service.createInstrumentFormGroup();

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdBy: expect.any(Object),
            createdOn: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedOn: expect.any(Object),
            deletedBy: expect.any(Object),
            deletedOn: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            boughtDate: expect.any(Object),
            price: expect.any(Object),
          })
        );
      });

      it('passing IInstrument should create a new form with FormGroup', () => {
        const formGroup = service.createInstrumentFormGroup(sampleWithRequiredData);

        expect(formGroup.controls).toEqual(
          expect.objectContaining({
            id: expect.any(Object),
            createdBy: expect.any(Object),
            createdOn: expect.any(Object),
            updatedBy: expect.any(Object),
            updatedOn: expect.any(Object),
            deletedBy: expect.any(Object),
            deletedOn: expect.any(Object),
            name: expect.any(Object),
            type: expect.any(Object),
            description: expect.any(Object),
            isActive: expect.any(Object),
            boughtDate: expect.any(Object),
            price: expect.any(Object),
          })
        );
      });
    });

    describe('getInstrument', () => {
      it('should return NewInstrument for default Instrument initial value', () => {
        // eslint-disable-next-line @typescript-eslint/no-unused-vars
        const formGroup = service.createInstrumentFormGroup(sampleWithNewData);

        const instrument = service.getInstrument(formGroup) as any;

        expect(instrument).toMatchObject(sampleWithNewData);
      });

      it('should return NewInstrument for empty Instrument initial value', () => {
        const formGroup = service.createInstrumentFormGroup();

        const instrument = service.getInstrument(formGroup) as any;

        expect(instrument).toMatchObject({});
      });

      it('should return IInstrument', () => {
        const formGroup = service.createInstrumentFormGroup(sampleWithRequiredData);

        const instrument = service.getInstrument(formGroup) as any;

        expect(instrument).toMatchObject(sampleWithRequiredData);
      });
    });

    describe('resetForm', () => {
      it('passing IInstrument should not enable id FormControl', () => {
        const formGroup = service.createInstrumentFormGroup();
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, sampleWithRequiredData);

        expect(formGroup.controls.id.disabled).toBe(true);
      });

      it('passing NewInstrument should disable id FormControl', () => {
        const formGroup = service.createInstrumentFormGroup(sampleWithRequiredData);
        expect(formGroup.controls.id.disabled).toBe(true);

        service.resetForm(formGroup, { id: null });

        expect(formGroup.controls.id.disabled).toBe(true);
      });
    });
  });
});
