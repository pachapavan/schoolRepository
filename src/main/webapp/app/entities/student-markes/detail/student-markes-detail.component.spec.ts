import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StudentMarkesDetailComponent } from './student-markes-detail.component';

describe('StudentMarkes Management Detail Component', () => {
  let comp: StudentMarkesDetailComponent;
  let fixture: ComponentFixture<StudentMarkesDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StudentMarkesDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ studentMarkes: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(StudentMarkesDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StudentMarkesDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load studentMarkes on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.studentMarkes).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
