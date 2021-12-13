jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IClassName, ClassName } from '../class-name.model';
import { ClassNameService } from '../service/class-name.service';

import { ClassNameRoutingResolveService } from './class-name-routing-resolve.service';

describe('ClassName routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: ClassNameRoutingResolveService;
  let service: ClassNameService;
  let resultClassName: IClassName | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(ClassNameRoutingResolveService);
    service = TestBed.inject(ClassNameService);
    resultClassName = undefined;
  });

  describe('resolve', () => {
    it('should return IClassName returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultClassName = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultClassName).toEqual({ id: 123 });
    });

    it('should return new IClassName if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultClassName = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultClassName).toEqual(new ClassName());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as ClassName })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultClassName = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultClassName).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
