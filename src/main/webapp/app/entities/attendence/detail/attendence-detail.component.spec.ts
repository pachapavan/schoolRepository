import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { AttendenceDetailComponent } from './attendence-detail.component';

describe('Attendence Management Detail Component', () => {
  let comp: AttendenceDetailComponent;
  let fixture: ComponentFixture<AttendenceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [AttendenceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ attendence: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(AttendenceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(AttendenceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load attendence on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.attendence).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
