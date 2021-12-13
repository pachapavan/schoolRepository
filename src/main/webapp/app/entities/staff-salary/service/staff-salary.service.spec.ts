import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IStaffSalary, StaffSalary } from '../staff-salary.model';

import { StaffSalaryService } from './staff-salary.service';

describe('StaffSalary Service', () => {
  let service: StaffSalaryService;
  let httpMock: HttpTestingController;
  let elemDefault: IStaffSalary;
  let expectedResult: IStaffSalary | IStaffSalary[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(StaffSalaryService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      salaryPaid: 0,
      month: 'AAAAAAA',
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign({}, elemDefault);

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a StaffSalary', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new StaffSalary()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a StaffSalary', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          salaryPaid: 1,
          month: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a StaffSalary', () => {
      const patchObject = Object.assign(
        {
          salaryPaid: 1,
          month: 'BBBBBB',
        },
        new StaffSalary()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of StaffSalary', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          salaryPaid: 1,
          month: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a StaffSalary', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addStaffSalaryToCollectionIfMissing', () => {
      it('should add a StaffSalary to an empty array', () => {
        const staffSalary: IStaffSalary = { id: 123 };
        expectedResult = service.addStaffSalaryToCollectionIfMissing([], staffSalary);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(staffSalary);
      });

      it('should not add a StaffSalary to an array that contains it', () => {
        const staffSalary: IStaffSalary = { id: 123 };
        const staffSalaryCollection: IStaffSalary[] = [
          {
            ...staffSalary,
          },
          { id: 456 },
        ];
        expectedResult = service.addStaffSalaryToCollectionIfMissing(staffSalaryCollection, staffSalary);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a StaffSalary to an array that doesn't contain it", () => {
        const staffSalary: IStaffSalary = { id: 123 };
        const staffSalaryCollection: IStaffSalary[] = [{ id: 456 }];
        expectedResult = service.addStaffSalaryToCollectionIfMissing(staffSalaryCollection, staffSalary);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(staffSalary);
      });

      it('should add only unique StaffSalary to an array', () => {
        const staffSalaryArray: IStaffSalary[] = [{ id: 123 }, { id: 456 }, { id: 42015 }];
        const staffSalaryCollection: IStaffSalary[] = [{ id: 123 }];
        expectedResult = service.addStaffSalaryToCollectionIfMissing(staffSalaryCollection, ...staffSalaryArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const staffSalary: IStaffSalary = { id: 123 };
        const staffSalary2: IStaffSalary = { id: 456 };
        expectedResult = service.addStaffSalaryToCollectionIfMissing([], staffSalary, staffSalary2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(staffSalary);
        expect(expectedResult).toContain(staffSalary2);
      });

      it('should accept null and undefined values', () => {
        const staffSalary: IStaffSalary = { id: 123 };
        expectedResult = service.addStaffSalaryToCollectionIfMissing([], null, staffSalary, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(staffSalary);
      });

      it('should return initial array if no StaffSalary is added', () => {
        const staffSalaryCollection: IStaffSalary[] = [{ id: 123 }];
        expectedResult = service.addStaffSalaryToCollectionIfMissing(staffSalaryCollection, undefined, null);
        expect(expectedResult).toEqual(staffSalaryCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
