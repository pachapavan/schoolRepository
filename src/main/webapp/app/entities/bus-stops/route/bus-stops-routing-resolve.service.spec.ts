jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IBusStops, BusStops } from '../bus-stops.model';
import { BusStopsService } from '../service/bus-stops.service';

import { BusStopsRoutingResolveService } from './bus-stops-routing-resolve.service';

describe('BusStops routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: BusStopsRoutingResolveService;
  let service: BusStopsService;
  let resultBusStops: IBusStops | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(BusStopsRoutingResolveService);
    service = TestBed.inject(BusStopsService);
    resultBusStops = undefined;
  });

  describe('resolve', () => {
    it('should return IBusStops returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBusStops = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBusStops).toEqual({ id: 123 });
    });

    it('should return new IBusStops if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBusStops = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultBusStops).toEqual(new BusStops());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as BusStops })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultBusStops = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultBusStops).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
