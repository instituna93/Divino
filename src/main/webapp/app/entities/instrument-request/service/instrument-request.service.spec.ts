import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IInstrumentRequest } from '../instrument-request.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../instrument-request.test-samples';

import { InstrumentRequestService, RestInstrumentRequest } from './instrument-request.service';

const requireRestSample: RestInstrumentRequest = {
  ...sampleWithRequiredData,
  createdOn: sampleWithRequiredData.createdOn?.toJSON(),
  updatedOn: sampleWithRequiredData.updatedOn?.toJSON(),
  deletedOn: sampleWithRequiredData.deletedOn?.toJSON(),
};

describe('InstrumentRequest Service', () => {
  let service: InstrumentRequestService;
  let httpMock: HttpTestingController;
  let expectedResult: IInstrumentRequest | IInstrumentRequest[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(InstrumentRequestService);
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

    it('should create a InstrumentRequest', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const instrumentRequest = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(instrumentRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a InstrumentRequest', () => {
      const instrumentRequest = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(instrumentRequest).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a InstrumentRequest', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of InstrumentRequest', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a InstrumentRequest', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addInstrumentRequestToCollectionIfMissing', () => {
      it('should add a InstrumentRequest to an empty array', () => {
        const instrumentRequest: IInstrumentRequest = sampleWithRequiredData;
        expectedResult = service.addInstrumentRequestToCollectionIfMissing([], instrumentRequest);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(instrumentRequest);
      });

      it('should not add a InstrumentRequest to an array that contains it', () => {
        const instrumentRequest: IInstrumentRequest = sampleWithRequiredData;
        const instrumentRequestCollection: IInstrumentRequest[] = [
          {
            ...instrumentRequest,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addInstrumentRequestToCollectionIfMissing(instrumentRequestCollection, instrumentRequest);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a InstrumentRequest to an array that doesn't contain it", () => {
        const instrumentRequest: IInstrumentRequest = sampleWithRequiredData;
        const instrumentRequestCollection: IInstrumentRequest[] = [sampleWithPartialData];
        expectedResult = service.addInstrumentRequestToCollectionIfMissing(instrumentRequestCollection, instrumentRequest);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(instrumentRequest);
      });

      it('should add only unique InstrumentRequest to an array', () => {
        const instrumentRequestArray: IInstrumentRequest[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const instrumentRequestCollection: IInstrumentRequest[] = [sampleWithRequiredData];
        expectedResult = service.addInstrumentRequestToCollectionIfMissing(instrumentRequestCollection, ...instrumentRequestArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const instrumentRequest: IInstrumentRequest = sampleWithRequiredData;
        const instrumentRequest2: IInstrumentRequest = sampleWithPartialData;
        expectedResult = service.addInstrumentRequestToCollectionIfMissing([], instrumentRequest, instrumentRequest2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(instrumentRequest);
        expect(expectedResult).toContain(instrumentRequest2);
      });

      it('should accept null and undefined values', () => {
        const instrumentRequest: IInstrumentRequest = sampleWithRequiredData;
        expectedResult = service.addInstrumentRequestToCollectionIfMissing([], null, instrumentRequest, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(instrumentRequest);
      });

      it('should return initial array if no InstrumentRequest is added', () => {
        const instrumentRequestCollection: IInstrumentRequest[] = [sampleWithRequiredData];
        expectedResult = service.addInstrumentRequestToCollectionIfMissing(instrumentRequestCollection, undefined, null);
        expect(expectedResult).toEqual(instrumentRequestCollection);
      });
    });

    describe('compareInstrumentRequest', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareInstrumentRequest(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareInstrumentRequest(entity1, entity2);
        const compareResult2 = service.compareInstrumentRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareInstrumentRequest(entity1, entity2);
        const compareResult2 = service.compareInstrumentRequest(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareInstrumentRequest(entity1, entity2);
        const compareResult2 = service.compareInstrumentRequest(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
