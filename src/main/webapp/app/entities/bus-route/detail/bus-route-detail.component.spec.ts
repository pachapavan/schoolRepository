import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BusRouteDetailComponent } from './bus-route-detail.component';

describe('BusRoute Management Detail Component', () => {
  let comp: BusRouteDetailComponent;
  let fixture: ComponentFixture<BusRouteDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BusRouteDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ busRoute: { id: 123 } }) },
        },
      ],
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
      expect(comp.busRoute).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
