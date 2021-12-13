import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { IBusStops, BusStops } from '../bus-stops.model';

import { BusStopsService } from './bus-stops.service';

describe('BusStops Service', () => {
  let service: BusStopsService;
  let httpMock: HttpTestingController;
  let elemDefault: IBusStops;
  let expectedResult: IBusStops | IBusStops[] | boolean | null;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
    });
    expectedResult = null;
    service = TestBed.inject(BusStopsService);
    httpMock = TestBed.inject(HttpTestingController);

    elemDefault = {
      id: 0,
      routeName: 'AAAAAAA',
      busStops: 'AAAAAAA',
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

    it('should create a BusStops', () => {
      const returnedFromService = Object.assign(
        {
          id: 0,
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.create(new BusStops()).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'POST' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should update a BusStops', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          routeName: 'BBBBBB',
          busStops: 'BBBBBB',
        },
        elemDefault
      );

      const expected = Object.assign({}, returnedFromService);

      service.update(expected).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PUT' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should partial update a BusStops', () => {
      const patchObject = Object.assign({}, new BusStops());

      const returnedFromService = Object.assign(patchObject, elemDefault);

      const expected = Object.assign({}, returnedFromService);

      service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

      const req = httpMock.expectOne({ method: 'PATCH' });
      req.flush(returnedFromService);
      expect(expectedResult).toMatchObject(expected);
    });

    it('should return a list of BusStops', () => {
      const returnedFromService = Object.assign(
        {
          id: 1,
          routeName: 'BBBBBB',
          busStops: 'BBBBBB',
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

    it('should delete a BusStops', () => {
      service.delete(123).subscribe(resp => (expectedResult = resp.ok));

      const req = httpMock.expectOne({ method: 'DELETE' });
      req.flush({ status: 200 });
      expect(expectedResult);
    });

    describe('addBusStopsToCollectionIfMissing', () => {
      it('should add a BusStops to an empty array', () => {
        const busStops: IBusStops = { id: 123 };
        expectedResult = service.addBusStopsToCollectionIfMissing([], busStops);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(busStops);
      });

      it('should not add a BusStops to an array that contains it', () => {
        const busStops: IBusStops = { id: 123 };
        const busStopsCollection: IBusStops[] = [
          {
            ...busStops,
          },
          { id: 456 },
        ];
        expectedResult = service.addBusStopsToCollectionIfMissing(busStopsCollection, busStops);
        expect(expectedResult).toHaveLength(2);
      });

      it("should add a BusStops to an array that doesn't contain it", () => {
        const busStops: IBusStops = { id: 123 };
        const busStopsCollection: IBusStops[] = [{ id: 456 }];
        expectedResult = service.addBusStopsToCollectionIfMissing(busStopsCollection, busStops);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(busStops);
      });

      it('should add only unique BusStops to an array', () => {
        const busStopsArray: IBusStops[] = [{ id: 123 }, { id: 456 }, { id: 80285 }];
        const busStopsCollection: IBusStops[] = [{ id: 123 }];
        expectedResult = service.addBusStopsToCollectionIfMissing(busStopsCollection, ...busStopsArray);
        expect(expectedResult).toHaveLength(3);
      });

      it('should accept varargs', () => {
        const busStops: IBusStops = { id: 123 };
        const busStops2: IBusStops = { id: 456 };
        expectedResult = service.addBusStopsToCollectionIfMissing([], busStops, busStops2);
        expect(expectedResult).toHaveLength(2);
        expect(expectedResult).toContain(busStops);
        expect(expectedResult).toContain(busStops2);
      });

      it('should accept null and undefined values', () => {
        const busStops: IBusStops = { id: 123 };
        expectedResult = service.addBusStopsToCollectionIfMissing([], null, busStops, undefined);
        expect(expectedResult).toHaveLength(1);
        expect(expectedResult).toContain(busStops);
      });

      it('should return initial array if no BusStops is added', () => {
        const busStopsCollection: IBusStops[] = [{ id: 123 }];
        expectedResult = service.addBusStopsToCollectionIfMissing(busStopsCollection, undefined, null);
        expect(expectedResult).toEqual(busStopsCollection);
      });
    });
  });

  afterEach(() => {
    httpMock.verify();
  });
});
