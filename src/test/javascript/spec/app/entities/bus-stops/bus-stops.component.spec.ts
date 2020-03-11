import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { BusStopsComponent } from 'app/entities/bus-stops/bus-stops.component';
import { BusStopsService } from 'app/entities/bus-stops/bus-stops.service';
import { BusStops } from 'app/shared/model/bus-stops.model';

describe('Component Tests', () => {
  describe('BusStops Management Component', () => {
    let comp: BusStopsComponent;
    let fixture: ComponentFixture<BusStopsComponent>;
    let service: BusStopsService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [BusStopsComponent]
      })
        .overrideTemplate(BusStopsComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(BusStopsComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(BusStopsService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new BusStops(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.busStops && comp.busStops[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
