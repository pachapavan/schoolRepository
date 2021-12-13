import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { BusRouteNameDetailComponent } from './bus-route-name-detail.component';

describe('BusRouteName Management Detail Component', () => {
  let comp: BusRouteNameDetailComponent;
  let fixture: ComponentFixture<BusRouteNameDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [BusRouteNameDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ busRouteName: { id: 123 } }) },
        },
      ],
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
      expect(comp.busRouteName).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
