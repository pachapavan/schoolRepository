jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IAttendence, Attendence } from '../attendence.model';
import { AttendenceService } from '../service/attendence.service';

import { AttendenceRoutingResolveService } from './attendence-routing-resolve.service';

describe('Attendence routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: AttendenceRoutingResolveService;
  let service: AttendenceService;
  let resultAttendence: IAttendence | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(AttendenceRoutingResolveService);
    service = TestBed.inject(AttendenceService);
    resultAttendence = undefined;
  });

  describe('resolve', () => {
    it('should return IAttendence returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAttendence = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAttendence).toEqual({ id: 123 });
    });

    it('should return new IAttendence if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAttendence = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultAttendence).toEqual(new Attendence());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Attendence })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultAttendence = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultAttendence).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
