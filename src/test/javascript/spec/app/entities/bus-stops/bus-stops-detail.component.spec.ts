import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { BusStopsDetailComponent } from 'app/entities/bus-stops/bus-stops-detail.component';
import { BusStops } from 'app/shared/model/bus-stops.model';

describe('Component Tests', () => {
  describe('BusStops Management Detail Component', () => {
    let comp: BusStopsDetailComponent;
    let fixture: ComponentFixture<BusStopsDetailComponent>;
    const route = ({ data: of({ busStops: new BusStops(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [BusStopsDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BusStopsDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BusStopsDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load busStops on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.busStops).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
