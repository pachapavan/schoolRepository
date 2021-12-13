import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StudentFeeDetailComponent } from './student-fee-detail.component';

describe('StudentFee Management Detail Component', () => {
  let comp: StudentFeeDetailComponent;
  let fixture: ComponentFixture<StudentFeeDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StudentFeeDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ studentFee: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(StudentFeeDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StudentFeeDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load studentFee on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.studentFee).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
