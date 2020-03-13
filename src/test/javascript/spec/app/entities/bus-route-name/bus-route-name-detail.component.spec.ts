import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { BusRouteNameDetailComponent } from 'app/entities/bus-route-name/bus-route-name-detail.component';
import { BusRouteName } from 'app/shared/model/bus-route-name.model';

describe('Component Tests', () => {
  describe('BusRouteName Management Detail Component', () => {
    let comp: BusRouteNameDetailComponent;
    let fixture: ComponentFixture<BusRouteNameDetailComponent>;
    const route = ({ data: of({ busRouteName: new BusRouteName(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [BusRouteNameDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(BusRouteNameDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(BusRouteNameDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load busRouteName on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.busRouteName).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
