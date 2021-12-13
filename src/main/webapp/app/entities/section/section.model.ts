import { IClassName } from 'app/entities/class-name/class-name.model';

export interface ISection {
  id?: number;
  name?: string | null;
  sectionNumber?: number | null;
  className?: IClassName | null;
}

export class Section implements ISection {
  constructor(
    public id?: number,
    public name?: string | null,
    public sectionNumber?: number | null,
    public className?: IClassName | null
  ) {}
}

export function getSectionIdentifier(section: ISection): number | undefined {
  return section.id;
}
