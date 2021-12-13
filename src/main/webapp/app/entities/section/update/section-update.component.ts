import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ISection, Section } from '../section.model';
import { SectionService } from '../service/section.service';
import { IClassName } from 'app/entities/class-name/class-name.model';
import { ClassNameService } from 'app/entities/class-name/service/class-name.service';

@Component({
  selector: 'jhi-section-update',
  templateUrl: './section-update.component.html',
})
export class SectionUpdateComponent implements OnInit {
  isSaving = false;

  classNamesSharedCollection: IClassName[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    sectionNumber: [],
    className: [],
  });

  constructor(
    protected sectionService: SectionService,
    protected classNameService: ClassNameService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ section }) => {
      this.updateForm(section);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const section = this.createFromForm();
    if (section.id !== undefined) {
      this.subscribeToSaveResponse(this.sectionService.update(section));
    } else {
      this.subscribeToSaveResponse(this.sectionService.create(section));
    }
  }

  trackClassNameById(index: number, item: IClassName): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISection>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(section: ISection): void {
    this.editForm.patchValue({
      id: section.id,
      name: section.name,
      sectionNumber: section.sectionNumber,
      className: section.className,
    });

    this.classNamesSharedCollection = this.classNameService.addClassNameToCollectionIfMissing(
      this.classNamesSharedCollection,
      section.className
    );
  }

  protected loadRelationshipsOptions(): void {
    this.classNameService
      .query()
      .pipe(map((res: HttpResponse<IClassName[]>) => res.body ?? []))
      .pipe(
        map((classNames: IClassName[]) =>
          this.classNameService.addClassNameToCollectionIfMissing(classNames, this.editForm.get('className')!.value)
        )
      )
      .subscribe((classNames: IClassName[]) => (this.classNamesSharedCollection = classNames));
  }

  protected createFromForm(): ISection {
    return {
      ...new Section(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      sectionNumber: this.editForm.get(['sectionNumber'])!.value,
      className: this.editForm.get(['className'])!.value,
    };
  }
}
