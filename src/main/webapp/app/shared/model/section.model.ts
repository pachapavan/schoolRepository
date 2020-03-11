import { IClassName } from 'app/shared/model/class-name.model';

export interface ISection {
  id?: number;
  name?: string;
  sectionNumber?: number;
  className?: IClassName;
}

export class Section implements ISection {
  constructor(public id?: number, public name?: string, public sectionNumber?: number, public className?: IClassName) {}
}
