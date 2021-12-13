import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClassNameDetailComponent } from './class-name-detail.component';

describe('ClassName Management Detail Component', () => {
  let comp: ClassNameDetailComponent;
  let fixture: ComponentFixture<ClassNameDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ClassNameDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ className: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ClassNameDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ClassNameDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load className on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.className).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
