import { TestBed, getTestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';
import { BusRouteService } from 'app/entities/bus-route/bus-route.service';
import { IBusRoute, BusRoute } from 'app/shared/model/bus-route.model';

describe('Service Tests', () => {
  describe('BusRoute Service', () => {
    let injector: TestBed;
    let service: BusRouteService;
    let httpMock: HttpTestingController;
    let elemDefault: IBusRoute;
    let expectedResult: IBusRoute | IBusRoute[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule]
      });
      expectedResult = null;
      injector = getTestBed();
      service = injector.get(BusRouteService);
      httpMock = injector.get(HttpTestingController);

      elemDefault = new BusRoute(0, 'AAAAAAA', 'AAAAAAA', 0, 0, 'AAAAAAA', 'AAAAAAA');
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a BusRoute', () => {
        const returnedFromService = Object.assign(
          {
            id: 0
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new BusRoute()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a BusRoute', () => {
        const returnedFromService = Object.assign(
          {
            routeName: 'BBBBBB',
            routeDriver: 'BBBBBB',
            busNumber: 1,
            year: 1,
            status: 'BBBBBB',
            comments: 'BBBBBB'
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of BusRoute', () => {
        const returnedFromService = Object.assign(
          {
            routeName: 'BBBBBB',
            routeDriver: 'BBBBBB',
            busNumber: 1,
            year: 1,
            status: 'BBBBBB',
            comments: 'BBBBBB'
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

      it('should delete a BusRoute', () => {
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
