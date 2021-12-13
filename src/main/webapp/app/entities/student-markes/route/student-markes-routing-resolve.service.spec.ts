jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { IStudentMarkes, StudentMarkes } from '../student-markes.model';
import { StudentMarkesService } from '../service/student-markes.service';

import { StudentMarkesRoutingResolveService } from './student-markes-routing-resolve.service';

describe('StudentMarkes routing resolve service', () => {
  let mockRouter: Router;
  let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
  let routingResolveService: StudentMarkesRoutingResolveService;
  let service: StudentMarkesService;
  let resultStudentMarkes: IStudentMarkes | undefined;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [HttpClientTestingModule],
      providers: [Router, ActivatedRouteSnapshot],
    });
    mockRouter = TestBed.inject(Router);
    mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
    routingResolveService = TestBed.inject(StudentMarkesRoutingResolveService);
    service = TestBed.inject(StudentMarkesService);
    resultStudentMarkes = undefined;
  });

  describe('resolve', () => {
    it('should return IStudentMarkes returned by find', () => {
      // GIVEN
      service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStudentMarkes = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultStudentMarkes).toEqual({ id: 123 });
    });

    it('should return new IStudentMarkes if id is not provided', () => {
      // GIVEN
      service.find = jest.fn();
      mockActivatedRouteSnapshot.params = {};

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStudentMarkes = result;
      });

      // THEN
      expect(service.find).not.toBeCalled();
      expect(resultStudentMarkes).toEqual(new StudentMarkes());
    });

    it('should route to 404 page if data not found in server', () => {
      // GIVEN
      jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as StudentMarkes })));
      mockActivatedRouteSnapshot.params = { id: 123 };

      // WHEN
      routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
        resultStudentMarkes = result;
      });

      // THEN
      expect(service.find).toBeCalledWith(123);
      expect(resultStudentMarkes).toEqual(undefined);
      expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
    });
  });
});
