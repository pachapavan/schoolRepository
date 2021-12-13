import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as dayjs from 'dayjs';

import { DATE_FORMAT } from 'app/config/input.constants';
import { IAttendence, Attendence } from '../attendence.model';

import { AttendenceService } from './attendence.service';

describe('Attendence Service', () => {
  let service: AttendenceService;
  let httpMock: HttpTestingController;
  let elemDefault: IAttendence;
  let expectedResult: IAttendence | IAttendence[] | boolean | null;
  let currentDate: dayjs.Dayjs;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(AttendenceService);
    httpMock = TestBed.inject(HttpTestingController);
    currentDate = dayjs();

    elemDefault = {
      id: 0,
      month: currentDate,
      totalWorkingDays: 0,
      dayspresent: 0,
    };
  });

  describe('Service methods', () => {
    it('should find an element', () => {
      const returnedFromService = Object.assign(
        {
          month: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      service.find(123).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(elemDefault);
    });

    it('should create a Attendence', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
          month: currentDate.format(DATE_FORMAT),
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          month: currentDate,
        },
        returnedFromService
      );

      service.create(new Attendence()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a Attendence', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          month: currentDate.format(DATE_FORMAT),
          totalWorkingDays: 1,
          dayspresent: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          month: currentDate,
        },
        returnedFromService
      );

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a Attendence', () => {
      const patchObject = Object.assign(
        {
          totalWorkingDays: 1,
          dayspresent: 1,
        },
        new Attendence()
      );

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign(
        {
          month: currentDate,
        },
        returnedFromService
      );

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of Attendence', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          month: currentDate.format(DATE_FORMAT),
          totalWorkingDays: 1,
          dayspresent: 1,
        },
        elemDefault
      );

      const expected = Object.assign(
        {
          month: currentDate,
        },
        returnedFromService
      );

      service.query().subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'GET' });
      req.flush([returnedFromService]);
      httpMock.verify();
      expect(expectedResult).toContainEqual(expected);
    });

    it('should delete a Attendence', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addAttendenceToCollectionIfMissing', () => {
      it('should add a Attendence to an empty array', () => {
        const attendence: IAttendence = { id: 123 };
        expectedResult = service.addAttendenceToCollectionIfMissing([], attendence);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attendence);
      });

      it('should not add a Attendence to an array that contains it', () => {
        const attendence: IAttendence = { id: 123 };
        const attendenceCollection: IAttendence[] = [
          {
            ...attendence,
          },
          { id: 456 },
        ];
        expectedResult = service.addAttendenceToCollectionIfMissing(attendenceCollection, attendence);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a Attendence to an array that doesn't contain it", () => {
        const attendence: IAttendence = { id: 123 };
        const attendenceCollection: IAttendence[] = [{ id: 456 }];
        expectedResult = service.addAttendenceToCollectionIfMissing(attendenceCollection, attendence);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attendence);
      });

      it('should add only unique Attendence to an array', () => {
        const attendenceArray: IAttendence[] = [{ id: 123 }, { id: 456 }, { id: 56810 }];
        const attendenceCollection: IAttendence[] = [{ id: 123 }];
        expectedResult = service.addAttendenceToCollectionIfMissing(attendenceCollection, ...attendenceArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const attendence: IAttendence = { id: 123 };
        const attendence2: IAttendence = { id: 456 };
        expectedResult = service.addAttendenceToCollectionIfMissing([], attendence, attendence2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(attendence);
        expect(expectedResult).toContain(attendence2);
      });

      it('should accept null and undefined values', () => {
        const attendence: IAttendence = { id: 123 };
        expectedResult = service.addAttendenceToCollectionIfMissing([], null, attendence, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(attendence);
      });

      it('should return initial array if no Attendence is added', () => {
        const attendenceCollection: IAttendence[] = [{ id: 123 }];
        expectedResult = service.addAttendenceToCollectionIfMissing(attendenceCollection, undefined, null);
        expect(expectedResult).toEqual(attendenceCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
