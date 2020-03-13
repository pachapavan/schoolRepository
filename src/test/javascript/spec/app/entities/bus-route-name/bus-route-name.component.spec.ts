import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { BusRouteNameComponent } from 'app/entities/bus-route-name/bus-route-name.component';
import { BusRouteNameService } from 'app/entities/bus-route-name/bus-route-name.service';
import { BusRouteName } from 'app/shared/model/bus-route-name.model';

describe('Component Tests', () => {
  describe('BusRouteName Management Component', () => {
    let comp: BusRouteNameComponent;
    let fixture: ComponentFixture<BusRouteNameComponent>;
    let service: BusRouteNameService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [BusRouteNameComponent]
      })
        .overrideTemplate(BusRouteNameComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BusRouteNameComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BusRouteNameService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new BusRouteName(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.busRouteNames && comp.busRouteNames[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
