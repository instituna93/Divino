import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IMemberTag } from '../member-tag.model';
import { sampleWithRequiredData, sampleWithNewData, sampleWithPartialData, sampleWithFullData } from '../member-tag.test-samples';

import { MemberTagService, RestMemberTag } from './member-tag.service';

const requireRestSample: RestMemberTag = {
  ...sampleWithRequiredData,
  createdOn: sampleWithRequiredData.createdOn?.toJSON(),
  updatedOn: sampleWithRequiredData.updatedOn?.toJSON(),
  deletedOn: sampleWithRequiredData.deletedOn?.toJSON(),
};

describe('MemberTag Service', () => {
  let service: MemberTagService;
  let httpMock: HttpTestingController;
  let expectedResult: IMemberTag | IMemberTag[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(MemberTagService);
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

    it('should create a MemberTag', () => {
      // eslint-disable-next-line @typescript-eslint/no-unused-vars
      const memberTag = { ...sampleWithNewData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.create(memberTag).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a MemberTag', () => {
      const memberTag = { ...sampleWithRequiredData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.update(memberTag).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a MemberTag', () => {
      const patchObject = { ...sampleWithPartialData };
      const returnedFromService = { ...requireRestSample };
      const expected = { ...sampleWithRequiredData };

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of MemberTag', () => {
      const returnedFromService = { ...requireRestSample };

      const expected = { ...sampleWithRequiredData };

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toMatchObject([expected]);
    });

    it('should delete a MemberTag', () => {
      const expected = true;

      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult).toBe(expected);
    });

    describe('addMemberTagToCollectionIfMissing', () => {
      it('should add a MemberTag to an empty array', () => {
        const memberTag: IMemberTag = sampleWithRequiredData;
        expectedResult = service.addMemberTagToCollectionIfMissing([], memberTag);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(memberTag);
      });

      it('should not add a MemberTag to an array that contains it', () => {
        const memberTag: IMemberTag = sampleWithRequiredData;
        const memberTagCollection: IMemberTag[] = [
          {
            ...memberTag,
          },
          sampleWithPartialData,
        ];
        expectedResult = service.addMemberTagToCollectionIfMissing(memberTagCollection, memberTag);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a MemberTag to an array that doesn't contain it", () => {
        const memberTag: IMemberTag = sampleWithRequiredData;
        const memberTagCollection: IMemberTag[] = [sampleWithPartialData];
        expectedResult = service.addMemberTagToCollectionIfMissing(memberTagCollection, memberTag);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(memberTag);
      });

      it('should add only unique MemberTag to an array', () => {
        const memberTagArray: IMemberTag[] = [sampleWithRequiredData, sampleWithPartialData, sampleWithFullData];
        const memberTagCollection: IMemberTag[] = [sampleWithRequiredData];
        expectedResult = service.addMemberTagToCollectionIfMissing(memberTagCollection, ...memberTagArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const memberTag: IMemberTag = sampleWithRequiredData;
        const memberTag2: IMemberTag = sampleWithPartialData;
        expectedResult = service.addMemberTagToCollectionIfMissing([], memberTag, memberTag2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(memberTag);
        expect(expectedResult).toContain(memberTag2);
      });

      it('should accept null and undefined values', () => {
        const memberTag: IMemberTag = sampleWithRequiredData;
        expectedResult = service.addMemberTagToCollectionIfMissing([], null, memberTag, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(memberTag);
      });

      it('should return initial array if no MemberTag is added', () => {
        const memberTagCollection: IMemberTag[] = [sampleWithRequiredData];
        expectedResult = service.addMemberTagToCollectionIfMissing(memberTagCollection, undefined, null);
        expect(expectedResult).toEqual(memberTagCollection);
      });
    });

    describe('compareMemberTag', () => {
      it('Should return true if both entities are null', () => {
        const entity1 = null;
        const entity2 = null;

        const compareResult = service.compareMemberTag(entity1, entity2);

        expect(compareResult).toEqual(true);
      });

      it('Should return false if one entity is null', () => {
        const entity1 = { id: 123 };
        const entity2 = null;

        const compareResult1 = service.compareMemberTag(entity1, entity2);
        const compareResult2 = service.compareMemberTag(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey differs', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 456 };

        const compareResult1 = service.compareMemberTag(entity1, entity2);
        const compareResult2 = service.compareMemberTag(entity2, entity1);

        expect(compareResult1).toEqual(false);
        expect(compareResult2).toEqual(false);
      });

      it('Should return false if primaryKey matches', () => {
        const entity1 = { id: 123 };
        const entity2 = { id: 123 };

        const compareResult1 = service.compareMemberTag(entity1, entity2);
        const compareResult2 = service.compareMemberTag(entity2, entity1);

        expect(compareResult1).toEqual(true);
        expect(compareResult2).toEqual(true);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
