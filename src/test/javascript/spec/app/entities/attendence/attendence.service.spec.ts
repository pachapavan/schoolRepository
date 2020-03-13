import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { AttendenceService } from 'app/entities/attendence/attendence.service';
import { IAttendence, Attendence } from 'app/shared/model/attendence.model';

describe('Service Tests', () => {
  describe('Attendence Service', () => {
    let injector: TestBed;
    let service: AttendenceService;
    let httpMock: HttpTestingController;
    let elemDefault: IAttendence;
    let expectedResult: IAttendence | IAttendence[] | boolean | null;
    let currentDate: moment.Moment;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(AttendenceService);
      httpMock = injector.get(HttpTestingController);
      currentDate = moment();

      elemDefault = new Attendence(0, currentDate, 0, 0);
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign(
          {
            month: currentDate.format(DATE_FORMAT)
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
            month: currentDate.format(DATE_FORMAT)
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            month: currentDate
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
            month: currentDate.format(DATE_FORMAT),
            totalWorkingDays: 1,
            dayspresent: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            month: currentDate
          },
          returnedFromService
        );

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Attendence', () => {
        const returnedFromService = Object.assign(
          {
            month: currentDate.format(DATE_FORMAT),
            totalWorkingDays: 1,
            dayspresent: 1
          },
          elemDefault
        );

        const expected = Object.assign(
          {
            month: currentDate
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
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
