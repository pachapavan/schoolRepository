import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { BusRouteDetailComponent } from 'app/entities/bus-route/bus-route-detail.component';
import { BusRoute } from 'app/shared/model/bus-route.model';

describe('Component Tests', () => {
  describe('BusRoute Management Detail Component', () => {
    let comp: BusRouteDetailComponent;
    let fixture: ComponentFixture<BusRouteDetailComponent>;
    const route = ({ data: of({ busRoute: new BusRoute(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [BusRouteDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BusRouteDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BusRouteDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load busRoute on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.busRoute).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
