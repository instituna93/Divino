import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IInstrument } from '../instrument.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../instrument.test-samples';

import { InstrumentService, RestInstrument } from './instrument.service';

const requireRestSample: RestInstrument = {
  ...sampleWithRequiredData,
  createdOn: sampleWithRequiredData.createdOn?.toJSON(),
  updatedOn: sampleWithRequiredData.updatedOn?.toJSON(),
  deletedOn: sampleWithRequiredData.deletedOn?.toJSON(),
  boughtDate: sampleWithRequiredData.boughtDate?.format(DATE_FORMAT),
};

describe('Instrument Service', () => {
  let service: InstrumentService;
  let httpMock: HttpTestingController;
  let expectedResult: IInstrument | IInstrument[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InstrumentService);
    httpMock = TestBed.inject(HttpTestingController);
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should create a Instrument', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const instrument = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(instrument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Instrument', () => {
      const instrument = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(instrument).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Instrument', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Instrument', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a Instrument', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInstrumentToCollectionIfMissing', () => {
      it('should add a Instrument to an empty array', () => {
        const instrument: IInstrument = sampleWithRequiredData;
        expectedResult = service.addInstrumentToCollectionIfMissing([], instrument);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(instrument);
      });

      it('should not add a Instrument to an array that contains it', () => {
        const instrument: IInstrument = sampleWithRequiredData;
        const instrumentCollection: IInstrument[] = [
          {
            ...instrument,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInstrumentToCollectionIfMissing(instrumentCollection, instrument);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Instrument to an array that doesn't contain it", () => {
        const instrument: IInstrument = sampleWithRequiredData;
        const instrumentCollection: IInstrument[] = [sampleWithPartialData];
        expectedResult = service.addInstrumentToCollectionIfMissing(instrumentCollection, instrument);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(instrument);
      });

      it('should add only unique Instrument to an array', () => {
        const instrumentArray: IInstrument[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const instrumentCollection: IInstrument[] = [sampleWithRequiredData];
        expectedResult = service.addInstrumentToCollectionIfMissing(instrumentCollection, ...instrumentArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const instrument: IInstrument = sampleWithRequiredData;
        const instrument2: IInstrument = sampleWithPartialData;
        expectedResult = service.addInstrumentToCollectionIfMissing([], instrument, instrument2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(instrument);
        expect(expectedResult).toContain(instrument2);
      });

      it('should accept null and undefined values', () => {
        const instrument: IInstrument = sampleWithRequiredData;
        expectedResult = service.addInstrumentToCollectionIfMissing([], null, instrument, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(instrument);
      });

      it('should return initial array if no Instrument is added', () => {
        const instrumentCollection: IInstrument[] = [sampleWithRequiredData];
        expectedResult = service.addInstrumentToCollectionIfMissing(instrumentCollection, undefined, null);
        expect(expectedResult).toEqual(instrumentCollection);
      });
    });

    describe('compareInstrument', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInstrument(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInstrument(entity1, entity2);
        const compareResult2 = service.compareInstrument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInstrument(entity1, entity2);
        const compareResult2 = service.compareInstrument(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInstrument(entity1, entity2);
        const compareResult2 = service.compareInstrument(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
