import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { BusRouteComponent } from 'app/entities/bus-route/bus-route.component';
import { BusRouteService } from 'app/entities/bus-route/bus-route.service';
import { BusRoute } from 'app/shared/model/bus-route.model';

describe('Component Tests', () => {
  describe('BusRoute Management Component', () => {
    let comp: BusRouteComponent;
    let fixture: ComponentFixture<BusRouteComponent>;
    let service: BusRouteService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [BusRouteComponent]
      })
        .overrideTemplate(BusRouteComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BusRouteComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BusRouteService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new BusRoute(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.busRoutes && comp.busRoutes[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
