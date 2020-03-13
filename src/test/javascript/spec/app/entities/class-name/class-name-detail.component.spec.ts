import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { JhipsterSampleApplicationTestModule } from '../../../test.module';
import { ClassNameDetailComponent } from 'app/entities/class-name/class-name-detail.component';
import { ClassName } from 'app/shared/model/class-name.model';

describe('Component Tests', () => {
  describe('ClassName Management Detail Component', () => {
    let comp: ClassNameDetailComponent;
    let fixture: ComponentFixture<ClassNameDetailComponent>;
    const route = ({ data: of({ className: new ClassName(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [JhipsterSampleApplicationTestModule],
        declarations: [ClassNameDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
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
        expect(comp.className).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
