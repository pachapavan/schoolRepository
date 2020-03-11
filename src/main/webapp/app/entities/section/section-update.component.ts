import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
// eslint-disable-next-line @typescript-eslint/no-unused-vars
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';

import { ISection, Section } from 'app/shared/model/section.model';
import { SectionService } from './section.service';
import { IClassName } from 'app/shared/model/class-name.model';
import { ClassNameService } from 'app/entities/class-name/class-name.service';

@Component({
  selector: 'jhi-section-update',
  templateUrl: './section-update.component.html'
})
export class SectionUpdateComponent implements OnInit {
  isSaving = false;
  classnames: IClassName[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    sectionNumber: [],
    className: []
  });

  constructor(
    protected sectionService: SectionService,
    protected classNameService: ClassNameService,
    protected activatedRoute: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ section }) => {
      this.updateForm(section);

      this.classNameService.query().subscribe((res: HttpResponse<IClassName[]>) => (this.classnames = res.body || []));
    });
  }

  updateForm(section: ISection): void {
    this.editForm.patchValue({
      id: section.id,
      name: section.name,
      sectionNumber: section.sectionNumber,
      className: section.className
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

  private createFromForm(): ISection {
    return {
      ...new Section(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      sectionNumber: this.editForm.get(['sectionNumber'])!.value,
      className: this.editForm.get(['className'])!.value
    };
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISection>>): void {
    result.subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  protected onSaveError(): void {
    this.isSaving = false;
  }

  trackById(index: number, item: IClassName): any {
    return item.id;
  }
}
