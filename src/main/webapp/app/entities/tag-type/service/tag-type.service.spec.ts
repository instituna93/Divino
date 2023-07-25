import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ITagType } from '../tag-type.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../tag-type.test-samples';

import { TagTypeService, RestTagType } from './tag-type.service';

const requireRestSample: RestTagType = {
  ...sampleWithRequiredData,
  createdOn: sampleWithRequiredData.createdOn?.toJSON(),
  updatedOn: sampleWithRequiredData.updatedOn?.toJSON(),
  deletedOn: sampleWithRequiredData.deletedOn?.toJSON(),
};

describe('TagType Service', () => {
  let service: TagTypeService;
  let httpMock: HttpTestingController;
  let expectedResult: ITagType | ITagType[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(TagTypeService);
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

    it('should create a TagType', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const tagType = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(tagType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a TagType', () => {
      const tagType = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(tagType).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a TagType', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of TagType', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a TagType', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addTagTypeToCollectionIfMissing', () => {
      it('should add a TagType to an empty array', () => {
        const tagType: ITagType = sampleWithRequiredData;
        expectedResult = service.addTagTypeToCollectionIfMissing([], tagType);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tagType);
      });

      it('should not add a TagType to an array that contains it', () => {
        const tagType: ITagType = sampleWithRequiredData;
        const tagTypeCollection: ITagType[] = [
          {
            ...tagType,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addTagTypeToCollectionIfMissing(tagTypeCollection, tagType);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a TagType to an array that doesn't contain it", () => {
        const tagType: ITagType = sampleWithRequiredData;
        const tagTypeCollection: ITagType[] = [sampleWithPartialData];
        expectedResult = service.addTagTypeToCollectionIfMissing(tagTypeCollection, tagType);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tagType);
      });

      it('should add only unique TagType to an array', () => {
        const tagTypeArray: ITagType[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const tagTypeCollection: ITagType[] = [sampleWithRequiredData];
        expectedResult = service.addTagTypeToCollectionIfMissing(tagTypeCollection, ...tagTypeArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const tagType: ITagType = sampleWithRequiredData;
        const tagType2: ITagType = sampleWithPartialData;
        expectedResult = service.addTagTypeToCollectionIfMissing([], tagType, tagType2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(tagType);
        expect(expectedResult).toContain(tagType2);
      });

      it('should accept null and undefined values', () => {
        const tagType: ITagType = sampleWithRequiredData;
        expectedResult = service.addTagTypeToCollectionIfMissing([], null, tagType, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(tagType);
      });

      it('should return initial array if no TagType is added', () => {
        const tagTypeCollection: ITagType[] = [sampleWithRequiredData];
        expectedResult = service.addTagTypeToCollectionIfMissing(tagTypeCollection, undefined, null);
        expect(expectedResult).toEqual(tagTypeCollection);
      });
    });

    describe('compareTagType', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareTagType(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareTagType(entity1, entity2);
        const compareResult2 = service.compareTagType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareTagType(entity1, entity2);
        const compareResult2 = service.compareTagType(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareTagType(entity1, entity2);
        const compareResult2 = service.compareTagType(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
